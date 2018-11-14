import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/shared/models/order'
@Component({
  selector: 'app-sub-overview',
  templateUrl: './sub-overview.component.html',
  styleUrls: ['./sub-overview.component.scss']
})
export class SubOverviewComponent implements OnInit {

  model: Order = {orderNumber: null, status: null, date: null};
  Orders: Order[] = [];

  
  constructor() { }


  ngOnInit() {
    this.addOrder();
  }


    /**
   * Add Order  to orders list.
   */
  public addOrder(): void{

    const order: Order = {orderNumber: this.model.orderNumber, status: this.model.status, date: this.model.date};
    this.Orders.push(order);
  }

}

