import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  tabs = [
    { path: './statistics', label: 'Statistics' },
    { path: './report', label: 'View Report and OEE'}
  ]

  constructor() { }

  ngOnInit() {
  }

}

