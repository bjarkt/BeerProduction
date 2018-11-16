import { Component, OnInit, ViewChild } from '@angular/core';
import { Order } from 'src/app/shared/models/order';
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { DataService } from 'src/app/shared/services/data.service';

@Component({
  selector: 'app-finished-orders',
  templateUrl: './finished-orders.component.html',
  styleUrls: ['./finished-orders.component.scss']
})
export class FinishedOrdersComponent implements OnInit {

  orders: Order[] = [];
  dataSource: MatTableDataSource<Order> = new MatTableDataSource();
  columnsToDisplay = ['orderNumber', 'date', 'status'];
  
  constructor(private data: DataService) { }

  ngOnInit() {
    this.loadOrders();
  }

  async loadOrders(){
    const res = await this.data.getOrders("done").toPromise();
    this.orders = res as Order[];
    this.dataSource.data = this.orders;
  }

}
