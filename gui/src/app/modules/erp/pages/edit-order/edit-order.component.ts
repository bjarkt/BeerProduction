import { Component, OnInit } from '@angular/core';
import { Routes, RouterModule, ParamMap, ActivatedRoute, Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { DataService } from 'src/app/shared/services/data.service';
import { Order } from 'src/app/shared/models/order';
import { OrderItem } from 'src/app/shared/models/orderItem';

@Component({
  selector: 'app-edit-order',
  templateUrl: './edit-order.component.html',
  styleUrls: ['./edit-order.component.scss']
})
export class EditOrderComponent implements OnInit {

  order: Order = { orderNumber: null, date: null, status: null };
  orderItems: OrderItem[] = [];

  constructor(private route: ActivatedRoute,
    private router: Router, private data: DataService) { }

  ngOnInit() {
    this.loadData();
  }

  async loadData() {
    const id = this.route.snapshot.paramMap.get('id');
    this.order = await this.data.getOrderDetails(id).toPromise();
    const orderItemsRes = await this.data.getOrderItems(this.order.orderNumber).toPromise();
    this.orderItems = orderItemsRes as OrderItem[];
  }




}
