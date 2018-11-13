import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { OrderItem } from 'src/app/shared/models/orderItem';
import { NgForm } from '@angular/forms';
import { DataService } from 'src/app/shared/services/data.service';

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.scss']
})
export class CreateOrderComponent implements OnInit {

  model:OrderItem = {orderNumber: null, beerName: null, status: null, quantity: null};
  orderItems: OrderItem[] = [];
  orderStatus: string;

  constructor(public data : DataService) { 
    this.orderStatus = 'Open';
  }

  ngOnInit() {
  }


  createOrder(){
    
  }

  /**
   * Method invoked when form is submitted.
   */
  onSubmit(form: NgForm){
    this.addOrderItem();
    form.reset();
  }

  /**
   * Add Order item to orderItems list.
   */
  public addOrderItem(): void{
    const orderItem: OrderItem = {orderNumber: -1, beerName: this.model.beerName, status: null, quantity: this.model.quantity};
    this.orderItems.push(orderItem);
  }

  /**
   * Clear orderItem form.
   */
  newOrderItem(){
    this.model = {orderNumber: null, beerName: null, status: null, quantity: null};
  } 

}
