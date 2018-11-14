import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { LoaderService } from '../shared/services/loading.service';

@Component({
  selector: 'app-core',
  templateUrl: './core.component.html',
  styleUrls: ['./core.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CoreComponent implements OnInit {

  constructor(public loaderService:LoaderService) { }

  ngOnInit() {
  }

}
