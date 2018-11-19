import { LocalDateTime } from './../../../../shared/models/localdatetime';
import { DataService } from 'src/app/shared/services/data.service';
import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { PageEvent, MatTableDataSource, MatPaginator, MatTable } from '@angular/material';

export interface MeasureElement {
  datetime: LocalDateTime;
  temperature: number;
  humidity: number;
  vibration: number;
}

export interface StateElement {
  phase: string;
  timeElapsed: number;
}

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.scss']
})

export class LogsComponent implements OnInit {
  totalRefresh: number;
  displayedMeasureColumns: String[] = ['datetime', 'temperature', 'humidity', 'vibration'];
  displayedStateColumns: String[] = ['phase', 'timeElapsed'];
  dataMeasureSource = new MatTableDataSource<MeasureElement>();
  dataStateSource = new MatTableDataSource<StateElement>();

  @ViewChild('measuretable') MeasureTable: MatTable<MeasureElement>;
  @ViewChild('statetable') stateTable: MatTable<StateElement>;
  @ViewChild('measurepaginator') measurePaginator: MatPaginator;
  @ViewChild('statepaginator') statePaginator: MatPaginator;
  
  constructor(private data: DataService) {
    this.totalRefresh = 0;
  }

  }

  ngOnInit() {
    this.dataMeasureSource.paginator = this.measurePaginator;
    this.dataStateSource.paginator = this.statePaginator;
  }

  refreshLogs() {
    this.totalRefresh++;

    // CHANGE THIS: REMOVE BYID TO GET CURRENT BATCH!
    this.data.getScadaLogsById(17).subscribe(res => {
      this.dataMeasureSource.data = [];
      this.dataStateSource.data = [];

      this.dataMeasureSource.data = this.getMeasurementLogs(res);
      this.dataStateSource.data = this.getStateLogs(res);
    });
    
    this.MeasureTable.renderRows();
    this.stateTable.renderRows();
  }

  getMeasurementLogs(res): MeasureElement[] {
    const measurements: MeasureElement[] = [];

    for (let i = 0; i < res['MeasurementLogs'].length; i++) {
      const measureLogs = res['MeasurementLogs'][i];
      const time = this.getMeasurementTime(measureLogs['measurementTime']);
      const measurement: MeasureElement = {
        datetime: time,
        temperature: measureLogs['measurements']['temperature'],
        humidity: measureLogs['measurements']['humidity'],
        vibration: measureLogs['measurements']['vibration'],
      };

      console.log(measurement.datetime);

      measurements.push(measurement);
    }

    return measurements;
  }

  getStateLogs(res): StateElement[] {
    const states: StateElement[] = [];

    for (let i = 0; i < res['StateTimeLogs'].length; i++) {
      const stateLogs = res['StateTimeLogs'][i];

      const state: StateElement = {
        phase: stateLogs['phase'],
        timeElapsed: stateLogs['timeElapsed']
      };

      states.push(state);
    }

    return states;
  }

  getMeasurementTime(measurementTime): LocalDateTime {
    const data: LocalDateTime = {
      dayOfYear: measurementTime['dayOfYear'],
      dayOfMonth: measurementTime['dayOfMonth'],
      dayOfWeek: measurementTime['dayOfWeek'],
      year: measurementTime['year'],
      month: measurementTime['month'],
      monthValue: measurementTime['monthValue'],
      hour: measurementTime['hour'],
      minute: measurementTime['minute'],
      second: measurementTime['second'],
      nano: measurementTime['nano'],
    };

    return data;
  }
}
