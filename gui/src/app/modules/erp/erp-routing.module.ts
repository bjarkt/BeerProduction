import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OrderManagementComponent } from './pages/order-management/order-management.component';
import { OpenOrdersComponent } from './pages/open-orders/open-orders.component';
import { LockedOrdersComponent } from './pages/locked-orders/locked-orders.component';
import { FinishedOrdersComponent } from './pages/finished-orders/finished-orders.component';
import { CreateOrderComponent } from './pages/create-order/create-order.component';

const routes: Routes = [
    {
      path: '',
      component: OrderManagementComponent,
      children: [
        {path: '', redirectTo: 'open-orders', pathMatch: 'full'},
        {path: 'open-orders', component: OpenOrdersComponent},
        {path: 'locked-orders', component: LockedOrdersComponent},
        {path: 'finished-orders', component: FinishedOrdersComponent},
        {path: 'create-order', component: CreateOrderComponent}
      ]
    }

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class ErpRoutingModule { }