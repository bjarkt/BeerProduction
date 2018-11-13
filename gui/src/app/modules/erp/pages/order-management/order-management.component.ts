import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-order-management',
  templateUrl: './order-management.component.html',
  styleUrls: ['./order-management.component.scss']
})
export class OrderManagementComponent implements OnInit {

  tabs = [
    { path: './open-orders', label: 'Open Orders' },
    { path: './locked-orders', label: 'Locked Orders' },
    { path: './finished-orders', label: 'Finished Orders' }
  ]

  constructor() { }

  ngOnInit() {
  }

}
