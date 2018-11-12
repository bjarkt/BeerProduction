import { NgModule } from '@angular/core';
import { CoreRoutingModule } from './core-routing.module';
import { RouterModule, RouterOutlet } from '@angular/router';
import { HeaderComponent } from './layout/header/header.component';

@NgModule({
    declarations: [
        HeaderComponent
    ],
    exports: [
        HeaderComponent
    ],
    imports: [ 
        RouterModule,
        CoreRoutingModule
    ],
    providers: []
  })
  export class CoreModule { }