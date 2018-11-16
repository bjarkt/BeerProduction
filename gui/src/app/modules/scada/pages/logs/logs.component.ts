import { Component, OnInit, ViewChild } from '@angular/core';
import { PageEvent, MatTableDataSource, MatPaginator, MatTable } from '@angular/material';

export interface LogElement {
  temperature: number;
  humidity: number;
  vibration: number;
}

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.scss']
})

export class LogsComponent implements OnInit {
  totalRefresh: number;
  elementData: LogElement[] = [];
  displayedColumns: String[] = ['temperature', 'humidity', 'vibration'];
  dataSource = new MatTableDataSource<LogElement>(this.elementData);
  @ViewChild(MatTable) table: MatTable<LogElement>;
  @ViewChild(MatPaginator) paginator: MatPaginator;
  
  constructor() {
    this.totalRefresh = 0;
  }

  ngOnInit() {
    for (let index = 0; index < 20; index++) {
      const data: LogElement = {
        temperature: (index + 1) * (this.totalRefresh + 1),
        humidity: (index + 1) * (this.totalRefresh + 1),
        vibration: (index + 1) * (this.totalRefresh + 1)
      };

      this.elementData.push(data);
    }

    this.dataSource.paginator = this.paginator;
  }

  refreshLogs() {
    this.totalRefresh++;
    this.elementData = [];

    for (let index = 0; index < 20; index++) {
      const data: LogElement = {
        temperature: (index + 1) * (this.totalRefresh + 1),
        humidity: (index + 1) * (this.totalRefresh + 1),
        vibration: (index + 1) * (this.totalRefresh + 1)
      };

      this.elementData.push(data);
    }

    this.dataSource.data = this.elementData;
    this.table.renderRows();
  }

}
