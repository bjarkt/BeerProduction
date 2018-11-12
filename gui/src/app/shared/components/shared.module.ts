import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubheaderComponent } from './subheader/subheader.component';
import { ButtonComponent } from './button/button.component';
@NgModule({
    declarations: [
        SubheaderComponent,
        ButtonComponent
    ],
    imports: [ 
        CommonModule        
    ],
    exports: [
        SubheaderComponent,
        ButtonComponent
    ],
    providers: []
  })
  export class SharedModule { }