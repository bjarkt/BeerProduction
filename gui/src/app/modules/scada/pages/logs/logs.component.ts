import { Component, OnInit, ViewChild, ViewChildren, QueryList } from '@angular/core';
import { PageEvent, MatTableDataSource, MatPaginator, MatTable } from '@angular/material';

export interface MeasureElement {
  datetime: string;
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
  elementMeasureData: MeasureElement[] = [];
  elementStateData: StateElement[] = [];
  displayedMeasureColumns: String[] = ['datetime', 'temperature', 'humidity', 'vibration'];
  displayedStateColumns: String[] = ['phase', 'timeElapsed'];
  dataMeasureSource = new MatTableDataSource<MeasureElement>(this.elementMeasureData);
  dataStateSource = new MatTableDataSource<StateElement>(this.elementStateData);

  @ViewChild('measuretable') MeasureTable: MatTable<MeasureElement>;
  @ViewChild('statetable') stateTable: MatTable<StateElement>;
  @ViewChild('measurepaginator') measurePaginator: MatPaginator;
  @ViewChild('statepaginator') statePaginator: MatPaginator;
  
  constructor() {
    this.totalRefresh = 0;
  }

  }

  ngOnInit() {
    this.addTemporaryMeasures();
    this.addTemporaryStates();

    this.dataMeasureSource.paginator = this.measurePaginator;
    this.dataStateSource.paginator = this.statePaginator;

    this.dataMeasureSource.data = this.elementMeasureData;
    this.dataStateSource.data = this.elementStateData;
  }

  addTemporaryMeasures() {
    this.elementMeasureData = [];

    for (let index = 0; index < 20; index++) {
      const data: MeasureElement = {
        datetime: '08-11-2018 12:00:00',
        temperature: (index + 1) * (this.totalRefresh + 1),
        humidity: (index + 1) * (this.totalRefresh + 1),
        vibration: (index + 1) * (this.totalRefresh + 1)
      };

      this.elementMeasureData.push(data);
    }
  }

  addTemporaryStates() {
    this.elementStateData = [];

    for (let index = 0; index < 7; index++) {
      const data: StateElement = {
        phase: 'Phase ' + index,
        timeElapsed: this.totalRefresh * index
      };

      this.elementStateData.push(data);
    }
  }

  refreshLogs() {
    this.totalRefresh++;

    this.addTemporaryMeasures();
    this.addTemporaryStates();

    this.dataMeasureSource.data = this.elementMeasureData;
    this.dataStateSource.data = this.elementStateData;
    
    this.MeasureTable.renderRows();
    this.stateTable.renderRows();
  }
}
