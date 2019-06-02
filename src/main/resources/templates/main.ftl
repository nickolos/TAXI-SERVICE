<#import "parts/common.ftl" as c>

<@c.page>
    <div class="collapse" id="collapseExample">
        <div class="form-group mt-3">
            <form method="post" action="/order/taxi" enctype="multipart/form-data">
                <div class="form-group">
                    <p>
                        <label>Input Time</label>
                        <label>
                            <input type="datetime-local"  class="form-control" format="yyyy-MM-dd HH:mm:ss" name ="datetime13">
                        </label>
                    </p>
                </div>
                <div class="form-group">
                    <p>
                        <label for="fromAddress">Input From Address</label>
                        <input id="fromAddress"  type="text" name="fromAddress">
                    </p>
                </div>
                <div class="form-group">
                        <p>
                            <label for="toAddress">Input To Address</label>
                            <input id="toAddress"  type="text" name="toAddress">
                        </p>
                    </div>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Create</button>
                </div>
            </form>
        </div>
</@c.page>