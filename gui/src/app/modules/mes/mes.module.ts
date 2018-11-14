import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MesRoutingModule } from './mes-routing.module';
import { OverviewComponent } from './pages/overview/overview.component';
import { SharedModule } from 'src/app/shared/components/shared.module';
import { ProductionPlanningComponent } from './pages/production-planning/production-planning.component';
import { StatisticsComponent } from './pages/statistics/statistics.component';
import { SubOverviewComponent } from './pages/sub-overview/sub-overview.component';

@NgModule({
    declarations: [
        
    OverviewComponent,
        
    ProductionPlanningComponent,
        
    StatisticsComponent,
        
    SubOverviewComponent],
    imports: [ 
        CommonModule,
        MesRoutingModule,
        SharedModule
    ],
    providers: []
  })
  export class MesModule { }