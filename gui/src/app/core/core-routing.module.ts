import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
const routes: Routes = [
    {
      path: '',
      redirectTo: '/erp/open-orders',
      pathMatch: 'full'
    },
    {
      path: 'erp',
      loadChildren: "../modules/erp/erp.module#ErpModule"
    },
    {
      path: 'mes',
      loadChildren: "../modules/mes/mes.module#MesModule"
    },
    {
      path: 'scada',
      loadChildren: "../modules/scada/scada.module#ScadaModule"
    },
    {
      path: '**',
      redirectTo: '/erp'
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
  })
  export class CoreRoutingModule { }