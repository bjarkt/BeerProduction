import { Component, OnInit } from '@angular/core';
import { Order } from 'src/app/shared/models/order'
import { DataService } from 'src/app/shared/services/data.service';
import { ProductionInfo } from 'src/app/shared/models/ProductionInfo'; 
import { OrderItem } from 'src/app/shared/models/OrderItem'; 
import { Observable } from 'rxjs';
@Component({
  selector: 'app-sub-overview',
  templateUrl: './sub-overview.component.html',
  styleUrls: ['./sub-overview.component.scss']
})
export class SubOverviewComponent implements OnInit {

  order: Order = {orderNumber: null, status: null, date: null};
  orders: Order[] = [];

  orderItems: OrderItem[] = [];

  productInfo: ProductionInfo = {recipeName: null, orderNumber: null, machineSpeed: null, quantity: null, batchId: null};
  batches: ProductionInfo[] = [];


  constructor(private data: DataService) { }


  ngOnInit() {
    this.addOrders();
    //this.orders.push({orderNumber: 123123, status: "processing", date: new Date()});
    //this.orderItems.push({beerName: "Ale", orderNumber: 123123, quantity: 10, status: "locked" })
    //this.orderItems.push({beerName: "Pilsner", orderNumber: 123123, quantity: 10, status: "locked" })


  }


    /**
   * Add Order  to orders list.
   */
  public addOrders(): void{
    this.data.getOrders().subscribe(result => {
      this.orders = result
      console.log(this.orders)
    })
  }

  public getOrderItems(event: any): void{
    this.data.getOrderItems(this.order.orderNumber).subscribe(result => {
      console.log(result)


      for (const key in result) {
        if (result.hasOwnProperty(key)) {
          const orderItem = result[key];
          console.log(key)
          this.batches.push({
            quantity: orderItem.quantity,
            orderNumber: orderItem.orderNumber,
            batchId: null, machineSpeed: null, 
            recipeName: orderItem.name 
          });        
        }
      }
    })

    for (let order of this.orders) {
      if(this.order.orderNumber === order.orderNumber){
        this.order.date = order.date
        this.order.status = order.status
        break;
      }
    }
  }

  public createBatch(){
    this.data.createBatches(this.batches);
    console.log(this.batches)
  }

}

