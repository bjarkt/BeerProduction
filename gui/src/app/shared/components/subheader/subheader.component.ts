import { Component, OnInit, Input, HostBinding } from '@angular/core';

@Component({
  selector: 'app-subheader',
  templateUrl: './subheader.component.html',
  styleUrls: ['./subheader.component.scss']
})
export class SubheaderComponent implements OnInit {

  @HostBinding('class.sticky') sticky : boolean = false;
  @Input() title: string;

  constructor() { 
    window.addEventListener("scroll", (event) => {
      if(window.scrollY >= 44){
        this.sticky = true;
      } else{
        this.sticky = false;
      }
    });
  }

 
  ngOnInit() {
  }

}
