import { CommonModule } from '@angular/common';
import { ErpRoutingModule } from './erp-routing.module';
import { NgModule } from '@angular/core';
import { OrderManagementComponent } from './pages/order-management/order-management.component';
import { OpenOrdersComponent } from './pages/open-orders/open-orders.component';
import { LockedOrdersComponent } from './pages/locked-orders/locked-orders.component';
import { FinishedOrdersComponent } from './pages/finished-orders/finished-orders.component';

@NgModule({
    declarations: [
        OrderManagementComponent,
        OpenOrdersComponent,
        LockedOrdersComponent,
        FinishedOrdersComponent
    ],
    imports: [ 
        CommonModule,
        ErpRoutingModule
    ],
    providers: []
  })
  export class ErpModule { }