import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { CoreComponent } from './core/core.component';
import { CoreModule } from './core/core.module';
import { RouterModule } from '@angular/router';
import { SharedModule } from './shared/components/shared.module';

@NgModule({
  declarations: [
    CoreComponent
  ],
  imports: [
    BrowserModule,
    CoreModule,
    RouterModule,
    SharedModule
  ],
  providers: [],
  bootstrap: [CoreComponent]
})
export class AppModule { }
