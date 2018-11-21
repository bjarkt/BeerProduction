import { Component, OnInit } from '@angular/core';
import { Batch } from 'src/app/shared/models/Batch';
import { Order } from 'src/app/shared/models/order';
import { MesDataService } from 'src/app/shared/services/mesData.service';
import { MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-sub-overview',
  templateUrl: './sub-overview.component.html',
  styleUrls: ['./sub-overview.component.scss']
})
export class SubOverviewComponent implements OnInit {

  batches: Batch[] = [];
  batchesDataSource: MatTableDataSource<Batch> = new MatTableDataSource();
  batchesColumnsToDisplay = ['batchId', 'orderNumber', 'beerName', 
                      'accepted', 'defect', 'started', 'finished'];

  orders: Order[] = [];
  ordersDataSource: MatTableDataSource<Order> = new MatTableDataSource();
  ordersColumnsToDisplay = ['orderNumber', 'date', 'status'];

  constructor(private data: MesDataService) { }

  ngOnInit() {
    this.loadData();
  }

  public async loadData() {
    this.batches = await this.data.getBatches().toPromise() as Batch[]
    this.batches.sort((a, b) => {
      if (a.batchId > b.batchId) return -1;
      else if (a.batchId < b.batchId) return 1;
      return 0;
    })
    this.batchesDataSource.data = this.batches;

    this.orders = await this.data.getMesOrders().toPromise() as Order[]
    this.orders.sort((a, b) => {
      if (a.orderNumber > b.orderNumber) return 1;
      else if (a.orderNumber < b.orderNumber) return -1;
      return 0;
    })
    this.ordersDataSource.data = this.orders
  }

}
