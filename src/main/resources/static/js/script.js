

const toggleSidebar = () =>{
	
	
	if($(".sidebar").is(":visible"))
	{
		$(".sidebar").css("dispaly","none");
		$(".content").css("margin-left","0%");
	}else{
		$(".sidebar").css("dispaly","block");
		$(".content").css("margin-left","20%");
	}
	
	
	
	
};


const search = ( )=>
{
	
	let query=$("#search-input").val();
	
	console.log(query);
	
	
	if(query=="")
	{
		$("search-result").hide();
		
	}else{
		
		//sending request to server
		
		let url=`http://localhost:8080/search/${query}`;
		
		fetch(url).then((response) => {
			
			return response.json()
			
			
		}).then((data) => {
			
		
			
			let text = `<div class='list-group'>`;
			
			data.forEach((contact) => {
				
				text += `<a href='/user/${contact.cId}/contact' class='list-group-item list-group-action'> ${contact.name} </a>` 
				
			});
			
			text += `</div>` ;
			
			$(".search-result").html(text);
			$(".search-result").show();
			
		});
		 
		
		}
	
};

//first request to server to create order


const paymentstart = () =>
{
	
	console.log("payment started");
	
	let amount=$("#payment_field").val();
	console.log(amount);
	
	if(amount=="" || amount == null)
	{
		alert("amount is required !!");
		return;
	}
	
	
	//we will use ajax to send request to server to create order
	$.ajax(
		{
			
			url:"/user/create_order",
			data:JSON.stringify({amount:amount,info:"order_request"}),
			contentType:"application/json",
			type:"POST",
			dataType:"json",
			success:function(response)
			{
				//invoked when sucess
				console.log(response);
				
				if(response.status=="created")
				{
					//open payment form
					let options = {
						
						key:"rzp_test_qz4OlqWmztsgCz" ,
						amount: response.amount,
						currency:"INR",
						name: "Smart Contact charity",
						description:"Donation",
						image:
						"data:image/png;base64,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",
						order_id: response.id,
						
						handler: function(response)
						{
							console.log(response.razorpay_payment_id);
							console.log(response.razorpay_order_id);
							console.log(razorpay_signature);
							console.log("payment successful !!");
							alert("payment successfull !!");
						},
						
						prefill: {
							
							
							name: "",
                            email: "",
                            contact: "",
                               },

						
		notes: {
     address: "learn code with mayur",
			
            },
					
		theme:{
			
			color:"#3399cc",
			
			
		},		
		};
		
		let rzp=new Razorpay(options);
		
		rzp.on("payment.failed", function (response){
console.log(response.error.code);
console.log(response.error.description);
console.log(response.error.source);
console.log(response.error.step);
console.log(response.error.reason);
console.log(response.error.metadata.order_id);
  console.log(response.error.metadata.payment_id);
  alert("oops payment failed !!");
  });
		rzp.open();
		}
		},
		
		
				
			
			error: function(error)
			{
				//invoked when error
				alert("something went wrong");
				
			},
			
			
			
		});
		
		
		
		
	
	
	
	
};






























