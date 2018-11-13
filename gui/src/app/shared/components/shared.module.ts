import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubheaderComponent } from './subheader/subheader.component';
import {MatButtonModule, MatTabsModule} from '@angular/material';
import { Routes, RouterModule } from '@angular/router';


@NgModule({
    declarations: [
        SubheaderComponent
    ],
    imports: [ 
        RouterModule,
        CommonModule,
        MatButtonModule,
        MatTabsModule    
    ],
    exports: [
        RouterModule,
        SubheaderComponent,
        MatButtonModule,
        MatTabsModule
    ],
    providers: []
  })
  export class SharedModule { }