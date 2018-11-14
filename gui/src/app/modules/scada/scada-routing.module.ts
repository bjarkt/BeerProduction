import { ScreenComponent } from './pages/screen/screen.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ScadaScreenComponent } from './pages/scada-screen/scada-screen.component';
import { LogsComponent } from './pages/logs/logs.component';

const routes: Routes = [
    {
      path: '',
      component: ScadaScreenComponent,
      children: [
        {path: '', redirectTo: 'screen', pathMatch: 'full'},
        {path: 'screen', component: ScreenComponent},
        {path: 'logs', component: LogsComponent}
      ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class ScadaRoutingModule { }