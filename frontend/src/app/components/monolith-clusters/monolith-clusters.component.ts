import { Component, ElementRef, ViewChild, AfterViewInit, Input, OnChanges } from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'app-monolith-clusters',
  templateUrl: './monolith-clusters.component.html',
  styleUrls: ['./monolith-clusters.component.sass']
})
export class MonolithClusters implements AfterViewInit, OnChanges {
  private svgContainer: ElementRef<HTMLDListElement>;

  @ViewChild('svgContainer', { static: false }) set content(content: ElementRef) {
    if (content) { // initially setter gets called with undefined
      this.svgContainer = content;
      this.ngAfterViewInit();
    }
  }

  @Input() decomposition: any;

  ngOnChanges() {
    console.log(this.decomposition);
    this.ngAfterViewInit();
  }

  ngAfterViewInit() {
    if (this.decomposition != undefined && this.svgContainer) {
      let width = 600,
        height = 600;

      let links = [];
      let nodes = [];

      let nrClusters = this.decomposition.serviceCuts.nrServices;

      for (let i = 0; i < this.decomposition.serviceCuts.services.length; i++) {
        let cluster = this.decomposition.serviceCuts.services[i];
        for (let j = 0; j < cluster.length; j++) {
          nodes.push({
            id: cluster[j].id,
            label: cluster[j].shortName,
            group: i + 1
          })
        }
      }
      console.log(nodes)

      for (let x = 0; x < this.decomposition.systemModel.links.length; x++) {
        let link = this.decomposition.systemModel.links[x];
        links.push({
          source: link.sourceNode.id,
          target: link.destinationNode.id,
          value: link.weight
        })
      }
      console.log(links)

      let simulation = d3.forceSimulation(nodes)
        .force("link", d3.forceLink(links).id((d) => { return d.id; }))
        .force("charge", d3.forceManyBody())
        .force("center", d3.forceCenter(width / 2, height / 2));

      if (this.svgContainer && this.svgContainer.nativeElement) {
        this.svgContainer.nativeElement.innerHTML = "";
      }

      let svg = d3.select(this.svgContainer.nativeElement).append('svg')
        .attr("id", "graph-svg")
        .attr("height", height)
        .attr("width", "100%")
        .attr("viewBox", [0, 0, width, height]);

      let link = svg.append("g")
        .attr("stroke", "#3E3E3E")
        .attr("stroke-opacity", 0.3)
        .selectAll("line")
        .data(links)
        .enter()
        .append("line")
        .attr("stroke-width", (d) => { return Math.sqrt(d.value) });

      let node = svg.append("g")
        .attr("stroke", "#3E3E3E")
        .attr("stroke-width", 2)
        .selectAll("circle")
        .data(nodes)
        .enter()
        .append("circle")
        .attr("r", 6)
        .attr("fill", color);

      let texts = svg.append("g")
        .selectAll("text")
        .data(nodes)
        .enter()
        .append("text").text(d => d.label)
        .attr("font-size", "11px")
        .attr("text-anchor", "middle")

      node.call(d3.drag()
        .on("start", (event) => {
          if (!event.active) {
            simulation.alphaTarget(0.3).restart();
          }
          event.subject.fx = event.subject.x;
          event.subject.fy = event.subject.y;
        })
        .on("drag", (event) => {
          event.subject.fx = event.x;
          event.subject.fy = event.y;
        })
        .on("end", (event) => {
          if (!event.active) {
            simulation.alphaTarget(0);
          }
          event.subject.fx = null;
          event.subject.fy = null;
        })
      );

      simulation.on("tick", ticked);

      function ticked() {
        texts
          .attr('x', (d) => { return d.x; })
          .attr('y', (d) => { return d.y; })

        node
          .attr("cx", d => d.x)
          .attr("cy", d => d.y);

        link
          .attr('x1', (d) => { return d.source.x; })
          .attr('y1', (d) => { return d.source.y; })
          .attr('x2', (d) => { return d.target.x; })
          .attr('y2', (d) => { return d.target.y; })
      }

      function color(d) {
        return d3.interpolateInferno(d.group / nrClusters)
      }

      window.addEventListener('resize', updateSize.bind(this));
      updateSize.call(this);

      function updateSize() {
        let newHeight = this.svgContainer.nativeElement.getBoundingClientRect().height
        let newWidth = this.svgContainer.nativeElement.getBoundingClientRect().width

        svg
          .attr("transform", "scale(" + newHeight / height + ")");
      }
    }
  }
}

