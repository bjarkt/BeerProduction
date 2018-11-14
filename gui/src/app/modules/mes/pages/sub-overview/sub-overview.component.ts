import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/shared/models/order'
import { DataService } from 'src/app/shared/services/data.service';
@Component({
  selector: 'app-sub-overview',
  templateUrl: './sub-overview.component.html',
  styleUrls: ['./sub-overview.component.scss']
})
export class SubOverviewComponent implements OnInit {

  model: Order = {orderNumber: null, status: null, date: null};
  orders: Order[] = [];

  
  constructor(private data: DataService) { }


  ngOnInit() {
    this.addOrders();
    this.orders.push({orderNumber: 123123, status: "processing", date: new Date()});
  }


    /**
   * Add Order  to orders list.
   */
  public addOrders(): void{
    this.data.getOrders().subscribe(result => {
      this.orders = result
      const order: Order = {orderNumber: this.model.orderNumber, status: this.model.status, date: this.model.date};
      this.orders.push(order);
    })


  }

}

