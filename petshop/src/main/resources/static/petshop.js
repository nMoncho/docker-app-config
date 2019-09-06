(function () {
  'use strict';

  function loadPictures () {
    let $pics = $('.js-pictures');

    $.get('/pictures').done(function (pics) {
      pics.forEach(function (pic) {
        $pics.append(`<img class="img-thumbnail col-3" src="${pic}">`);
      });
    }).fail(function () {
      alert('Something went wrong!');
    });
  }

  function loadControls () {
    let $form = $('form');
    let $submit = $form.find('button[type=submit]');
    let $picture = $form.find('input[name=picture]');

    $picture.change(function () {
      $submit.prop('disabled', $picture.val() ? false : true);
    });
  }

  $(document).ready(function () {
    loadPictures();
    loadControls();
  });

}());
