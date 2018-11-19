import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OverviewComponent } from './pages/overview/overview.component';
import { StatisticsComponent } from './pages/statistics/statistics.component';
import { CreateBatchesComponent } from './pages/create-batches/create-batches.component';
import { ReportComponent } from './pages/report/report.component';

const routes: Routes = [
    {
      path: '',
      component: OverviewComponent,
      children: [
        {path: '', redirectTo: 'sub-overview', pathMatch: 'full'},
        {path: 'statistics', component: StatisticsComponent},
        {path: 'create-batches', component: CreateBatchesComponent},
        {path: 'report', component: ReportComponent}
      ]
    }

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class MesRoutingModule { }

