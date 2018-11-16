import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OverviewComponent } from './pages/overview/overview.component';
import { ProductionPlanningComponent } from './/pages/production-planning/production-planning.component';
import { StatisticsComponent } from './pages/statistics/statistics.component';
import { SubOverviewComponent } from './pages/sub-overview/sub-overview.component';
import { CreateBatchesComponent } from './pages/create-batches/create-batches.component';

const routes: Routes = [
    {
      path: '',
      component: OverviewComponent,
      children: [
        {path: '', redirectTo: 'sub-overview', pathMatch: 'full'},
        {path: 'production-planning', component: ProductionPlanningComponent},
        {path: 'statistics', component: StatisticsComponent},
        {path: 'sub-overview', component: SubOverviewComponent},
        {path: 'create-batches', component: CreateBatchesComponent}
      ]
    }

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
  })
  export class MesRoutingModule { }

