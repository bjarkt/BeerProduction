import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MesRoutingModule } from './mes-routing.module';
import { OverviewComponent } from './pages/overview/overview.component';
import { SharedModule } from 'src/app/shared/components/shared.module';
import { StatisticsComponent } from './pages/statistics/statistics.component';
import { MesDataService } from 'src/app/shared/services/mesData.service';
import { CreateBatchesComponent } from './pages/create-batches/create-batches.component';
import { ReportComponent } from './pages/report/report.component';
import { SubOverviewComponent } from './pages/sub-overview/sub-overview.component';
@NgModule({
    declarations: [
        
    OverviewComponent,
        
        
    StatisticsComponent,
        
        
    CreateBatchesComponent,
        
    ReportComponent,
        
    SubOverviewComponent],
    imports: [ 
        CommonModule,
        MesRoutingModule,
        SharedModule
    ],
    providers: [MesDataService]
  })
  export class MesModule { }