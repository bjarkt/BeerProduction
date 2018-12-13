import { Component, OnInit } from "@angular/core";
import { Order } from "src/app/shared/models/order";
import { DataService } from "src/app/shared/services/data.service";
import { ProductionInfo } from "src/app/shared/models/ProductionInfo";
import { OrderItem } from "src/app/shared/models/orderItem";
import { Message } from "src/app/shared/models/message";
import { Observable } from "rxjs";
import { Recipe } from "src/app/shared/models/recipe";
import { MesDataService } from "src/app/shared/services/mesData.service";
import { MatSnackBar } from "@angular/material";
import { Router } from '@angular/router';

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
  machspeeds: Map<string, string[]> = new Map();

  disableCreateOrderBtn: boolean;

  constructor(private data: MesDataService, private snackBar: MatSnackBar, private router: Router) {}

  ngOnInit() {
    this.addOrders();
    this.disableCreateOrderBtn = false;
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
    this.data.getMesOrderItems(this.order.orderNumber).subscribe(result => {
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

      this.batches.forEach(batch => {
        var speeds = new Array();
        this.data.getSavingMachSpeed(batch.recipeName).subscribe(res => {
          speeds.push(res);
          this.data.getProfitableMachSpeed(batch.recipeName).subscribe(res2 => {
            speeds.push(res2);
            this.data.getFastestMachSpeed(batch.recipeName, batch.quantity).subscribe(res3 => {
              speeds.push(res3);
              this.machspeeds.set(batch.recipeName, speeds);
            })
          })
        })
      });
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
    this.disableCreateOrderBtn = true;
    this.data.createBatches(this.batches).subscribe(res => {
      const message = res as Message;
      this.disableCreateOrderBtn = false;
      this.snackBar.open(message.message, 'Dismiss', { duration: 4000 });
      this.router.navigate(['/mes/overview']);
    })
  }

  async setMachSpeed(i, priority : string, quantity?: number){
    var machSpeed = -1;
    var beerName = this.batches[i].recipeName

    if(priority == 'saving'){
      const res = await this.data.getSavingMachSpeed(beerName).toPromise();
      machSpeed = res;
    } else if(priority == 'profitable'){
      const res = await this.data.getProfitableMachSpeed(beerName).toPromise();
      machSpeed = res;
    } else if(priority == 'fastest'){
      const res = await this.data.getFastestMachSpeed(beerName, this.batches[i].quantity).toPromise();
      machSpeed = res;
    }

    this.batches[i].machineSpeed = machSpeed;
  }
}
