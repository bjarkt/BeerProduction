import { Component, OnInit } from '@angular/core';
import { Batch } from 'src/app/shared/models/Batch';
import { Order } from 'src/app/shared/models/order';

@Component({
  selector: 'app-sub-overview',
  templateUrl: './sub-overview.component.html',
  styleUrls: ['./sub-overview.component.scss']
})
export class SubOverviewComponent implements OnInit {

  batches: Batch[] = [];
  orders: Order[] = [];

  constructor() { }

  ngOnInit() {
    // fetch batch, order
  }

}
