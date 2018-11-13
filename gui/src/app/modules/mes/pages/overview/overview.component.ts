import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  tabs = [
    { path: './', label: 'Overview' },
    { path: './', label: 'Production Planning' },
    { path: './', label: 'Statistics' }
  ]

  constructor() { }

  ngOnInit() {
  }

}
