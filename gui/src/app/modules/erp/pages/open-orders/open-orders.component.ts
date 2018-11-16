import { Component, OnInit, ViewChild, OnChanges } from '@angular/core';
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
  dataSource: MatTableDataSource<Order>;
  columnsToDisplay = ['orderNumber', 'date', 'status', 'button'];

  constructor(private data: DataService, private snackBar: MatSnackBar) {   
    this.dataSource = new MatTableDataSource(this.orders);
  }

  ngOnInit() {
    this.loadOrders();
  }

  async loadOrders(){
    const res = await this.data.getOrders("open").toPromise();
    this.orders = res as Order[];
    this.dataSource.data = res as Order[];
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
