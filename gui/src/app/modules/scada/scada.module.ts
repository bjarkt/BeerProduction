import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ScadaRoutingModule } from './scada-routing.module';
import { ScadaScreenComponent } from './pages/scada-screen/scada-screen.component';

@NgModule({
    declarations: [
        
    ScadaScreenComponent],
    imports: [ 
        CommonModule,
        ScadaRoutingModule
    ],
    providers: []
  })
  export class ScadaModule { }