import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  tabs = [
    { path: './sub-overview', label: 'Overview' },
    { path: './production-planning', label: 'Production Planning' },
    { path: './statistics', label: 'Statistics' }
  ]

  constructor() { }

  ngOnInit() {
  }

}

