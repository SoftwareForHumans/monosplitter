import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-navigation-bar',
  templateUrl: './navigation-bar.component.html',
  styleUrls: ['./navigation-bar.component.sass']
})
export class NavigationBarComponent implements OnInit {
  private aboutClassName = ''
          decompositionClassName = 'active';
  constructor() { }

  ngOnInit() {
  }

  itemClick(item){
    switch(item){
      case 'ABOUT':
        this.aboutClassName = 'active';
        this.decompositionClassName = '';
        break;
      case 'DECOMPOSITION':
        this.decompositionClassName = 'active';
        this.aboutClassName = '';
        break;
      default:
        this.decompositionClassName = '';
        this.aboutClassName = '';
        break;
    }
  }
}
