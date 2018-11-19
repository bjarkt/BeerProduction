import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MesRoutingModule } from './mes-routing.module';
import { OverviewComponent } from './pages/overview/overview.component';
import { SharedModule } from 'src/app/shared/components/shared.module';
import { ProductionPlanningComponent } from './pages/production-planning/production-planning.component';
import { StatisticsComponent } from './pages/statistics/statistics.component';
import { SubOverviewComponent } from './pages/sub-overview/sub-overview.component';
<<<<<<< HEAD
import { MesDataService } from 'src/app/shared/services/mesData.service';
=======
import { DataService } from 'src/app/shared/services/data.service';
>>>>>>> dc116a0f05eb825beecb168dbe3953ec497f0aaa
import { CreateBatchesComponent } from './pages/create-batches/create-batches.component';
@NgModule({
    declarations: [
        
    OverviewComponent,
        
    ProductionPlanningComponent,
        
    StatisticsComponent,
        
    SubOverviewComponent,
        
    CreateBatchesComponent],
    imports: [ 
        CommonModule,
        MesRoutingModule,
        SharedModule
    ],
<<<<<<< HEAD
    providers: [MesDataService]
=======
    providers: [DataService]
>>>>>>> dc116a0f05eb825beecb168dbe3953ec497f0aaa
  })
  export class MesModule { }