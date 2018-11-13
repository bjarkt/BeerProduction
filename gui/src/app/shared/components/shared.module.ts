import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubheaderComponent } from './subheader/subheader.component';
import {MatButtonModule, MatTabsModule} from '@angular/material';


@NgModule({
    declarations: [
        SubheaderComponent
    ],
    imports: [ 
        CommonModule,
        MatButtonModule,
        MatTabsModule    
    ],
    exports: [
        SubheaderComponent,
        MatButtonModule,
        MatTabsModule
    ],
    providers: []
  })
  export class SharedModule { }