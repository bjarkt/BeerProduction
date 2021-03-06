import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ScadaRoutingModule } from './scada-routing.module';
import { ScadaScreenComponent } from './pages/scada-screen/scada-screen.component';
import { SharedModule } from 'src/app/shared/components/shared.module';
import { ScreenComponent } from './pages/screen/screen.component';
import { LogsComponent } from './pages/logs/logs.component';
import { DataService } from 'src/app/shared/services/data.service';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';

@NgModule({
    declarations: [    
        ScadaScreenComponent,
        ScreenComponent,
        LogsComponent
    ],
    imports: [ 
        CommonModule,
        ScadaRoutingModule,
        SharedModule,
        MatPaginatorModule,
        MatTableModule
    ],
    providers: [DataService]
  })
  export class ScadaModule { }