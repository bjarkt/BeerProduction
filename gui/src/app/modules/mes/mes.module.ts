import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MesRoutingModule } from './mes-routing.module';
import { OverviewComponent } from './pages/overview/overview.component';

@NgModule({
    declarations: [
        
    OverviewComponent],
    imports: [ 
        CommonModule,
        MesRoutingModule
    ],
    providers: []
  })
  export class MesModule { }