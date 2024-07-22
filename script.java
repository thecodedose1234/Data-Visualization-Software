// Sample data
let data = [
    {name: 'A', value: 30},
    {name: 'B', value: 80},
    {name: 'C', value: 45},
    {name: 'D', value: 60},
    {name: 'E', value: 20},
    {name: 'F', value: 90},
    {name: 'G', value: 55}
];

// Set up the dimensions and margins of the graph
const margin = {top: 20, right: 30, bottom: 50, left: 60},
    width = 800 - margin.left - margin.right,
    height = 500 - margin.top - margin.bottom;

// Append the svg object to the div called 'chart'
const svg = d3.select("#chart")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", translate(${margin.left},${margin.top}));

// X axis
const x = d3.scaleBand()
    .range([0, width])
    .domain(data.map(d => d.name))
    .padding(0.2);
svg.append("g")
    .attr("transform", translate(0, ${height}))
    .call(d3.axisBottom(x));

// Add Y axis
const y = d3.scaleLinear()
    .domain([0, 100])
    .range([height, 0]);
svg.append("g")
    .call(d3.axisLeft(y));

// Create a tooltip
const tooltip = d3.select("body").append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

// Create the bars
const bars = svg.selectAll("rect")
    .data(data)
    .enter()
    .append("rect")
    .attr("x", d => x(d.name))
    .attr("width", x.bandwidth())
    .attr("fill", "#69b3a2")
    .attr("height", d => height - y(0)) // start height from 0
    .attr("y", d => y(0))
    .on("mouseover", (event, d) => {
        tooltip.transition().duration(200).style("opacity", .9);
        tooltip.html(Value: ${d.value})
            .style("left", ${event.pageX}px)
            .style("top", ${event.pageY - 28}px);
    })
    .on("mouseout", () => {
        tooltip.transition().duration(500).style("opacity", 0);
    })
    .transition()
    .duration(800)
    .attr("y", d => y(d.value))
    .attr("height", d => height - y(d.value));

// Update filter value and filter the data
function updateFilter(value) {
    document.getElementById('filter-value').textContent = value;
    const filteredData = data.filter(d => d.value >= value);
    updateChart(filteredData);
}

// Function to update the chart
function updateChart(filteredData) {
    x.domain(filteredData.map(d => d.name));
    svg.selectAll("rect")
        .data(filteredData, d => d.name)
        .join(
            enter => enter.append("rect")
                .attr("x", d => x(d.name))
                .attr("width", x.bandwidth())
                .attr("fill", "#69b3a2")
                .attr("height", d => height - y(0))
                .attr("y", d => y(0))
                .on("mouseover", (event, d) => {
                    tooltip.transition().duration(200).style("opacity", .9);
                    tooltip.html(Value: ${d.value})
                        .style("left", ${event.pageX}px)
                        .style("top", ${event.pageY - 28}px);
                })
                .on("mouseout", () => {
                    tooltip.transition().duration(500).style("opacity", 0);
                })
                .transition()
                .duration(800)
                .attr("y", d => y(d.value))
                .attr("height", d => height - y(d.value)),
            update => update
                .transition()
                .duration(800)
                .attr("x", d => x(d.name))
                .attr("y", d => y(d.value))
                .attr("height", d => height - y(d.value)),
            exit => exit.remove()
        );
    svg.select("g").call(d3.axisBottom(x));
}

// Function to add data from user input
function addData() {
    const name = document.getElementById('name').value;
    const value = document.getElementById('value').value;
    if (name && value) {
        data.push({name, value: +value});
        updateChart(data);
        document.getElementById('name').value = '';
        document.getElementById('value').value = '';
    }
}