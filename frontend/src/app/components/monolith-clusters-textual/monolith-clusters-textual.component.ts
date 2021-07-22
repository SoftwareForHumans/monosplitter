import { FlatTreeControl } from '@angular/cdk/tree';
import { Component, Input, OnChanges } from '@angular/core';
import { MatTreeFlatDataSource, MatTreeFlattener } from '@angular/material/tree';

interface Cluster {
  name: string;
  children?: Cluster[];
}

interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  level: number;
}

@Component({
  selector: 'app-monolith-clusters-textual',
  templateUrl: './monolith-clusters-textual.component.html',
  styleUrls: ['./monolith-clusters-textual.component.sass']
})

export class MonolithClustersTextualComponent implements OnChanges {
  @Input() decomposition: any;
  private TREE_DATA: Cluster[] = [];

  private _transformer = (node: Cluster, level: number) => {
    return {
      expandable: !!node.children && node.children.length > 0,
      name: node.name,
      level: level,
    };
  }

  treeControl = new FlatTreeControl<ExampleFlatNode>(
    node => node.level, node => node.expandable);

  treeFlattener = new MatTreeFlattener(
    this._transformer, node => node.level, node => node.expandable, node => node.children);

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  hasChild = (_: number, node: ExampleFlatNode) => node.expandable;

  constructor() { }

  ngOnChanges() {
    if (this.decomposition && this.decomposition.serviceCuts) {
      let clusters = this.decomposition.serviceCuts.services;

      for (let i = 0; i < clusters.length; i++) {

        this.TREE_DATA.push({
          name: "Service " + (i + 1),
          children: []
        })

        let cluster = clusters[i];

        for (let j = 0; j < cluster.length; j++) {
          this.TREE_DATA[i].children.push({
            name: cluster[j].shortName
          })
        }
      }

      this.dataSource.data = this.TREE_DATA;
    }
  }
}
