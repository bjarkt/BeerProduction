import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-scada-screen',
  templateUrl: './scada-screen.component.html',
  styleUrls: ['./scada-screen.component.scss']
})
export class ScadaScreenComponent implements OnInit {

  tabs = [
    { path: './screen', label: 'Screen' },
    { path: './logs', label: 'Logs' }
  ];

  constructor() { }

  ngOnInit() {
  }

}
