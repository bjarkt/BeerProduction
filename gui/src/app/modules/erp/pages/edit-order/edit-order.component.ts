import { Component, OnInit, ViewChild, ElementRef, ViewChildren, QueryList } from '@angular/core';
import { Routes, RouterModule, ParamMap, ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { DataService } from 'src/app/shared/services/data.service';
import { Order } from 'src/app/shared/models/order';
import { OrderItem } from 'src/app/shared/models/orderItem';
import { MatSnackBar } from '@angular/material';
import { element } from '@angular/core/src/render3';

@Component({
  selector: 'app-edit-order',
  templateUrl: './edit-order.component.html',
  styleUrls: ['./edit-order.component.scss']
})
export class EditOrderComponent implements OnInit {

  order: Order = { orderNumber: null, date: null, status: null };
  orderItems: OrderItem[] = [];
  quantity: number;
  @ViewChildren('quantityInput') inputs: QueryList<ElementRef>;

  constructor(private route: ActivatedRoute,
    private router: Router, private data: DataService, private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.loadData();
  }

  async loadData() {
    const id = this.route.snapshot.paramMap.get('id');
    this.order = await this.data.getOrderDetails(id).toPromise();
    const orderItemsRes = await this.data.getOrderItems(this.order.orderNumber).toPromise();
    this.orderItems = orderItemsRes as OrderItem[];
  }

  async updateOrderItem(orderItem: OrderItem) {
  
    this.inputs.forEach(element => {
      if (element.nativeElement.id = orderItem.beerName) {
        this.quantity = element.nativeElement.value;
      }
    })
    console.log(this.quantity);

    const res = await this.data.updateOrderItem(orderItem.orderNumber, orderItem.beerName, this.quantity).toPromise();
    this.snackBar.open(res.message, 'Dismiss', {duration:4000});
    this.loadData();
  }

  async deleteOrderItem(orderItem: OrderItem) {
    const res = await this.data.deleteOrderItem(orderItem.orderNumber, orderItem.beerName).toPromise();
    this.snackBar.open(res.message, 'Dismiss', {duration:4000});
    this.loadData();
  }



}
