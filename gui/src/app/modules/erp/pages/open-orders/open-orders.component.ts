import { Component, OnInit, ViewChild } from '@angular/core';
import { Order } from 'src/app/shared/models/order';
import { DataService } from 'src/app/shared/services/data.service';
import { MatTableDataSource, MatPaginator, MatSort, MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-open-orders',
  templateUrl: './open-orders.component.html',
  styleUrls: ['./open-orders.component.scss']
})
export class OpenOrdersComponent implements OnInit {

  orders: Order[] = [];
  dataSource: MatTableDataSource<Order> = new MatTableDataSource();
  @ViewChild(MatPaginator) paginator: MatPaginator;

  columnsToDisplay = ['orderNumber', 'date', 'status', 'button'];
  @ViewChild(MatSort) sort: MatSort; 

  constructor(private data: DataService, private snackBar: MatSnackBar) {   }

  ngOnInit() {
    this.loadOrders();
  }

  async loadOrders(){
    const res = await this.data.getOrders("open").toPromise();
    this.orders = res as Order[];
    this.dataSource.data = this.orders;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  async deleteOrder(order:Order){
    const res = await this.data.deleteOrder(order.orderNumber).toPromise();
    this.snackBar.open(res.message, 'Dismiss', { duration: 4000 });
    this.loadOrders();
  }

  async lockOrder(order: Order){
    const res = await this.data.lockOrder(order.orderNumber).toPromise()
    this.snackBar.open(res.message, 'Dismiss', { duration: 4000 });
    this.loadOrders();
  }

}
