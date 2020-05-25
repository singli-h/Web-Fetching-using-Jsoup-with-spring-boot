//   liSing 
/*
function myFunction() {
  document.getElementByClassName("trr").style.backgroundColor = "red";
}*/

var app = new Vue({
  el: '#app',
  data:
  {

    searchQuery: "",
    columns:
    {
      title: 'Album',
      artist: 'Artist',
      price:'Price', 
      rank: 'Rank'
    },

    rows: [{title: "Test Title 1", artist: "Billie Eilish", price: "5.0",rank: "#1" },],
    curSort:'rank', 
    curSortDir:'asce',  
    arrowPos: 'Rank'
  },
  

  created: function() 
  {
    fetch('http://localhost/link/albums').then(function(response){
      if (response.ok){
        return response.json();
      }
      throw new Error('Network Response is not ok')
    })
      .then(myJson => {
      this.rows = myJson;
    })
      .catch(function(error){
      console.log('There is sth wrong with ur fetch operation. ', error.message);
    });
  },
  
  methods:
  {
//     sorting function
    sorting: function (clickedCol)
    {
      console.log('sorting column %s', clickedCol);
      // identify the required sorting column, its direction, and arrow position
      this.arrowPos = clickedCol;
      switch(clickedCol)  
      {
        case 'Album':
          this.curSort = 'title';
          break;
        case 'Artist':
          this.curSort = 'artist';
          break;
        case 'Price':
          this.curSort = 'price';
          break;
        default:
          this.curSort = 'rank';
      }
      this.curSortDir = this.curSortDir === 'asce'? 'desc':'asce';
    }
  },
    
  computed: {
     filteredData: function () {
         var filterKey = this.searchQuery && this.searchQuery.toLowerCase();
         var data = this.rows;
         if (filterKey) {
             data = data.filter(function (row) {
                 return Object.keys(row).some(function (key) {
                     return String(row[key]).toLowerCase().indexOf(filterKey) > -1;
                 });
             });
         }
         return data.sort((a,b) => {
                    let modifier = 1;

                    if(this.curSortDir === 'desc') 
                      modifier = -1;

                    if(a[this.curSort] < b[this.curSort]) 
                      return -1 * modifier;

                    if(a[this.curSort] > b[this.curSort])
                      return 1 * modifier;

                   return 0;
                  });
     }
  }
});