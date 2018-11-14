import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ScadaRoutingModule } from './scada-routing.module';
import { ScadaScreenComponent } from './pages/scada-screen/scada-screen.component';
import { SharedModule } from 'src/app/shared/components/shared.module';
import { ScreenComponent } from './pages/screen/screen.component';
import { LogsComponent } from './pages/logs/logs.component';

@NgModule({
    declarations: [
        
    ScadaScreenComponent,
        
    ScreenComponent,
        
    LogsComponent],
    imports: [ 
        CommonModule,
        ScadaRoutingModule,
        SharedModule
    ],
    providers: []
  })
  export class ScadaModule { }