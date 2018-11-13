import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MesRoutingModule } from './mes-routing.module';
import { OverviewComponent } from './pages/overview/overview.component';
import { SharedModule } from 'src/app/shared/components/shared.module';

@NgModule({
    declarations: [
        
    OverviewComponent],
    imports: [ 
        CommonModule,
        MesRoutingModule,
        SharedModule
    ],
    providers: []
  })
  export class MesModule { }