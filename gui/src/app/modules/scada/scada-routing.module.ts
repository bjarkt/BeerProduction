import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ScadaScreenComponent } from './pages/scada-screen/scada-screen.component';

const routes: Routes = [
    {
      path: '',
      component: ScadaScreenComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class ScadaRoutingModule { }