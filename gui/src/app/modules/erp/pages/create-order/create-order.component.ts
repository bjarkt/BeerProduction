import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { OrderItem } from 'src/app/shared/models/orderItem';
import { NgForm } from '@angular/forms';
import { DataService } from 'src/app/shared/services/data.service';
import { MatSnackBar, MatSnackBarRef } from '@angular/material';
import { Router } from '@angular/router';

export interface BeerSelect {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.scss']
})
export class CreateOrderComponent implements OnInit {

  model: OrderItem = { orderNumber: null, beerName: null, status: null, quantity: null };
  orderItems: OrderItem[] = [];
  options: BeerSelect[];
  orderNumber: number;

  constructor(private data: DataService, private snackBar: MatSnackBar, private router: Router) {
    this.options = [
      { value: 'pilsner', viewValue: 'Pilsner' },
      { value: 'ale', viewValue: 'Ale' },
      { value: 'stout', viewValue: 'Stout' },
      { value: 'wheat', viewValue: 'Wheat' },
      { value: 'ipa', viewValue: 'IPA' },
      { value: 'alcohol free', viewValue: 'Alcohol Free' },
    ];
  }

  ngOnInit() {
  }

  /**
   * Creates order and inserts order items.
   */
   async createOrder(){
    const newOrderNumber = await this.data.createOrder().toPromise();
    this.orderNumber = newOrderNumber.message;

    if(this.orderItems.length > 0){
      for(const orderitem of this.orderItems){
        await this.data.addOrderItem(this.orderNumber, orderitem.beerName, orderitem.quantity).toPromise();
      }
    }

    this.snackBar.open("Order created with order number " + this.orderNumber, 'Dismiss', { duration: 4000 });
    this.router.navigate(['/erp/open-orders']);
  }

  /**
   * Method invoked when form is submitted.
   */
  onSubmit(form: NgForm) {
    this.addOrderItem();
    form.reset();
  }

  /**
   * Add Order item to orderItems list.
   */
  public addOrderItem(): void {
    const orderItem: OrderItem = { orderNumber: -1, beerName: this.model.beerName, status: null, quantity: this.model.quantity };
    for (var i = this.options.length - 1; i >= 0; --i) {
      if (this.options[i].value == this.model.beerName) {
        this.options.splice(i, 1);
      }
    }
    this.orderItems.push(orderItem);
  }

  /**
   * Clear orderItem form.
   */
  newOrderItem() {
    this.model = { orderNumber: null, beerName: null, status: null, quantity: null };
  }

  deleteOrderItem(orderitem) {
    this.orderItems.splice(this.orderItems.indexOf(orderitem), 1);
    const beerSelect: BeerSelect = { value: orderitem.beerName, viewValue: orderitem.beerName }
    this.options.push(beerSelect);
  }

}
