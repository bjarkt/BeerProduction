import { CommonModule} from '@angular/common';
import { ErpRoutingModule } from './erp-routing.module';
import { NgModule } from '@angular/core';
import { OrderManagementComponent } from './pages/order-management/order-management.component';
import { OpenOrdersComponent } from './pages/open-orders/open-orders.component';
import { LockedOrdersComponent } from './pages/locked-orders/locked-orders.component';
import { FinishedOrdersComponent } from './pages/finished-orders/finished-orders.component';
import { SharedModule } from 'src/app/shared/components/shared.module';
import { CreateOrderComponent } from './pages/create-order/create-order.component';
import { DataService } from 'src/app/shared/services/data.service';

@NgModule({
    declarations: [
        OrderManagementComponent,
        OpenOrdersComponent,
        LockedOrdersComponent,
        FinishedOrdersComponent,
        CreateOrderComponent
    ],
    imports: [ 
        CommonModule,
        ErpRoutingModule,
        SharedModule
    ],
    providers: [DataService]
  })
  export class ErpModule { }