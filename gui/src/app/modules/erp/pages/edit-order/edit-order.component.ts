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

  order: Order = { orderNumber: null, date: null, status: null};
  orderItems: OrderItem[] = [];

  constructor(private route: ActivatedRoute,
    private router: Router, private data: DataService) { }

  ngOnInit() {
    this.loadData();
  }

  async loadData(){
    this.route.paramMap.pipe(
      switchMap(async (params: ParamMap) =>
        await this.data.getOrderDetails(params.get('id')).toPromise()
      )
    ).subscribe(res => this.order = res as Order);

    const orderItemsRes = await this.data.getOrderItems(this.order.orderNumber).toPromise();
    this.orderItems = orderItemsRes as OrderItem[];
    console.log(this.orderItems);
  }

  


}
