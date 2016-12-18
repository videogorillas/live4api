/*global module:false*/
module.exports = function(grunt) {
    var jsFiles = [ "target/classes/stjs.js","src/main/js/ns.js", "target/classes/live4api3.js", "src/main/js/export.js"];

    grunt
            .initConfig({
                watch : {
                    scripts : {
                        files : jsFiles,
                        tasks : [ 'default' ]
                    }
                },
                concat : {
                    "bundle" : {
                        src : jsFiles,
                        dest : "live4api.js"
                    }
                }
            });

    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-watch');

    grunt.registerTask('default', [ 'concat:bundle']);
};
