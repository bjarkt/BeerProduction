import { Component, OnInit, ViewChild } from '@angular/core';
import { PageEvent, MatTableDataSource, MatPaginator } from '@angular/material';

export interface LogElement {
  temperature: number;
  humidity: number;
  vibration: number;
}

const DATA: LogElement[] = [];

for (let index = 0; index < 20; index++) {
  const data: LogElement = {
    temperature: Math.random() * 5 * (index + 1),
    humidity: Math.random() * 5 * (index + 1),
    vibration: Math.random() * 2 * (index + 1)
  };

  DATA.push(data);
}

@Component({
  selector: 'app-logs',
  templateUrl: './logs.component.html',
  styleUrls: ['./logs.component.scss']
})

export class LogsComponent implements OnInit {
  displayedColumns: String[] = ['temperature', 'humidity', 'vibration'];
  dataSource = new MatTableDataSource<LogElement>(DATA);
  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor() {

  }

  ngOnInit() {
    this.dataSource.paginator = this.paginator;
  }

}
