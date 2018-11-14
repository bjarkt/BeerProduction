import { Component, OnInit, ViewChild } from '@angular/core';
import { Order } from 'src/app/shared/models/order';
import { DataService } from 'src/app/shared/services/data.service';
import { MatTableDataSource, MatPaginator } from '@angular/material';

@Component({
  selector: 'app-open-orders',
  templateUrl: './open-orders.component.html',
  styleUrls: ['./open-orders.component.scss']
})
export class OpenOrdersComponent implements OnInit {

  orders: Order[] = [];
  dataSource: MatTableDataSource<Order>;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  columnsToDisplay = ['orderNumber', 'date', 'status'];

  constructor(private data: DataService) { }

  ngOnInit() {
    this.data.getOrders("open").subscribe(res => {
      if(res != null){
        this.orders = res as Order[];
        this.dataSource = new MatTableDataSource(this.orders);
        this.dataSource.paginator = this.paginator;
      }
    })
  }
}
