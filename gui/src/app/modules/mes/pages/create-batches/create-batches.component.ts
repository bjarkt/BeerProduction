import { Component, OnInit } from "@angular/core";
import { Order } from "src/app/shared/models/order";
import { DataService } from "src/app/shared/services/data.service";
import { ProductionInfo } from "src/app/shared/models/ProductionInfo";
import { OrderItem } from "src/app/shared/models/orderItem";
import { Observable } from "rxjs";
import { Recipe } from "src/app/shared/models/recipe";
@Component({
  selector: "app-create-batches",
  templateUrl: "./create-batches.component.html",
  styleUrls: ["./create-batches.component.scss"]
})
export class CreateBatchesComponent implements OnInit {
  order: Order = { orderNumber: null, status: null, date: null };
  orders: Order[] = [];

  orderItems: OrderItem[] = [];

  productInfo: ProductionInfo = {
    recipeName: null,
    orderNumber: null,
    machineSpeed: null,
    quantity: null,
    batchId: null
  };
  batches: ProductionInfo[] = [];
  recipes: Recipe[] = [];

  constructor(private data: DataService) {}

  ngOnInit() {
    this.addOrders();
    //this.orders.push({orderNumber: 123123, status: "processing", date: new Date()});
    //this.orderItems.push({beerName: "Ale", orderNumber: 123123, quantity: 10, status: "locked" })
    //this.orderItems.push({beerName: "Pilsner", orderNumber: 123123, quantity: 10, status: "locked" })
  }

  /**
   * Add Order  to orders list.
   */
  public addOrders(): void {
    this.data.getMesOrders().subscribe(result => {
      this.orders = result;
    });
  }

  public getOrderItems(event: any): void {
    this.data.getOrderItems(this.order.orderNumber).subscribe(result => {
      this.batches = [];
      this.recipes = [];
      for (let i = 0; i < result.OrderItems.length; i++) {
        let orderItem = result.OrderItems[i];
        this.batches.push({
          quantity: orderItem.quantity,
          orderNumber: orderItem.orderNumber,
          batchId: null,
          machineSpeed: null,
          recipeName: orderItem.beerName
        });
        this.recipes.push(((result.Recipe) as Recipe[]).find(r => r.name === orderItem.beerName));
      }
    });

    for (let order of this.orders) {
      if (this.order.orderNumber === order.orderNumber) {
        this.order.date = order.date;
        this.order.status = order.status;
        break;
      }
    }
  }

  public createBatch() {
    this.data.createBatches(this.batches).toPromise();
  }
}
