import { Component, OnInit, ViewChild } from '@angular/core';
import { Order } from 'src/app/shared/models/order';
import { MatTableDataSource, MatPaginator, MatSort } from '@angular/material';
import { DataService } from 'src/app/shared/services/data.service';

@Component({
  selector: 'app-locked-orders',
  templateUrl: './locked-orders.component.html',
  styleUrls: ['./locked-orders.component.scss']
})
export class LockedOrdersComponent implements OnInit {

  orders: Order[] = [];
  dataSource: MatTableDataSource<Order> = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator;

  columnsToDisplay = ['orderNumber', 'date', 'status'];
  @ViewChild(MatSort) sort: MatSort; 

  
  constructor(private data: DataService) { }

  ngOnInit() {
    this.loadOrders();
  }

  async loadOrders(){
    const res = await this.data.getOrders("locked").toPromise();
    this.orders = res as Order[];
    this.dataSource.data = this.orders;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

}
